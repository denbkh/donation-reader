package reader;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


import com.google.common.base.Strings;
import com.google.gson.*;
import freemarker.template.Configuration;
import okhttp3.*;
import okhttp3.Request;
import okhttp3.Response;
import ro.pippo.core.*;
import ro.pippo.core.route.CSRFHandler;
import ro.pippo.core.route.RouteContext;
import ro.pippo.freemarker.FreemarkerTemplateEngine;

public class Start extends Application {
    private static final String CONFIG_FILE_NOT_FOUND = "config file not found";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String ACCESS_TOKEN = "access_token";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson =
            new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public Start() {

        setTemplateEngine(new FreemarkerTemplateEngineEx());
    }

    @Override
    protected void onInit() {
        PippoSettings settings = getPippoSettings();

        clientId = settings.getString(CLIENT_ID, CONFIG_FILE_NOT_FOUND);
        clientSecret = settings.getString(CLIENT_SECRET, CONFIG_FILE_NOT_FOUND);
        redirectUri = settings.getString(REDIRECT_URI, CONFIG_FILE_NOT_FOUND);

        GET("/.*", routeContext -> {
            routeContext.setLocal("mode", RuntimeMode.getCurrent());
            routeContext.next();
        });

        ANY("/.*", new CSRFHandler());

        GET("/", routeContext -> {

            Cookie streamLabRefreshToken = routeContext.getRequest().getCookie(REFRESH_TOKEN);
            if (streamLabRefreshToken == null || Strings.isNullOrEmpty(streamLabRefreshToken.getValue())) {
                routeContext.redirect("/authorize");
            } else {
                routeContext.redirect("/realtime");
            }

        });

        GET("/authorize", routeContext -> {
            String codeParam = routeContext.getRequest().getParameter("code").toString();
            String csrfParam = routeContext.getRequest().getParameter("state").toString();
            HttpUrl authorizeUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("www.streamlabs.com")
                    .addEncodedPathSegments("api/v1.0/authorize")
                    .addQueryParameter("client_id", clientId)
                    .addQueryParameter("redirect_uri", redirectUri)
                    .addEncodedQueryParameter("response_type", "code")
                    .addEncodedQueryParameter("scope", "donations.read+socket.token")
                    .addEncodedQueryParameter("state", routeContext.getSession(CSRFHandler.TOKEN))
                    .build();

            routeContext.setLocal("authorizeUrl", authorizeUrl);

            if (codeParam == null) {
                routeContext.render("authorize");
            } else {
                //got redirect from streamlab

                //manual csrf validation on GET redirect ...
                String sessionToken = routeContext.getSession(CSRFHandler.TOKEN);
                if (!csrfParam.equals(sessionToken)) {
                    throw new StatusCodeException(HttpServletResponse.SC_FORBIDDEN, "CSRF token mismatch");
                }

                try {
                    GetTokenResponse getTokenResponse = invokeGetToken(codeParam, null);
                    setCookie(routeContext, REFRESH_TOKEN, getTokenResponse.refreshToken, Integer.MAX_VALUE);
                    setCookie(routeContext, ACCESS_TOKEN, getTokenResponse.accessToken, getTokenResponse.expiresIn);

                    routeContext.redirect("/realtime");
                } catch (IOException e) {
                    routeContext.setLocal("error", e.getMessage());
                    routeContext.render("authorize");
                }
            }

        });

        GET("/realtime", routeContext -> {
            Cookie refreshTokenCookie = routeContext.getRequest().getCookie(REFRESH_TOKEN);
            if (refreshTokenCookie == null) {
                routeContext.redirect("/authorize");
                return;
            }

            routeContext.render("realtime");
        });

        POST("/realtime", routeContext -> {
            Cookie refreshTokenCookie = routeContext.getRequest().getCookie(REFRESH_TOKEN);
            if (refreshTokenCookie == null) {
                routeContext.status(500);
                routeContext.send("error: can't find refresh_token cookie");
                return;
            }

            Cookie accessTokenCookie = routeContext.getRequest().getCookie(ACCESS_TOKEN);
            if (accessTokenCookie != null) {
                routeContext.send("access token is not expired");
                return;
            }

            try {
                GetTokenResponse getTokenResponse = invokeGetToken(null, refreshTokenCookie.getValue());
                setCookie(routeContext, REFRESH_TOKEN, getTokenResponse.refreshToken, Integer.MAX_VALUE);
                setCookie(routeContext, ACCESS_TOKEN, getTokenResponse.accessToken, getTokenResponse.expiresIn);

                routeContext.send("access token updated");
            } catch (IOException e) {
                routeContext.status(500);
                routeContext.send("error: " + e.getMessage());
            }
        });

        GET("/history", routeContext -> {
            routeContext.render("history");
        });

        addPublicResourceRoute();
    }

    private void setCookie(RouteContext routeContext, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        routeContext.getResponse().cookie(cookie);
    }

    private GetTokenResponse invokeGetToken(String authCode, String refreshToken) throws IOException {
        FormBody.Builder builder = new FormBody.Builder()
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("redirect_uri", redirectUri);
        if (authCode == null) {
            builder.addEncoded("grant_type", REFRESH_TOKEN);
            builder.add(REFRESH_TOKEN, refreshToken);
        } else {
            builder.addEncoded("grant_type", "authorization_code");
            builder.add("code", authCode);
        }

        Request request = new Request.Builder()
                .url("https://streamlabs.com/api/v1.0/token")
                .post(builder.build())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response.toString());
            }

            return gson.fromJson(response.body().charStream(), GetTokenResponse.class);
        }
    }

    private static class GetTokenResponse {
        String accessToken;
        String tokenType;
        String refreshToken;
        int expiresIn;
    }

    private static class FreemarkerTemplateEngineEx extends FreemarkerTemplateEngine {
        @Override
        protected void init(Application application, Configuration configuration) {
            configuration.setIncompatibleImprovements(Configuration.VERSION_2_3_27);
            setFileExtension("ftlh");
        }
    }
}
