<#macro siteLayout>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Donation Reader</title>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css"/>
</head>

<body>
    <nav class="navbar navbar-default">
        <div class="container-fluid">

            <div class="navbar-header">
                <span class="navbar-brand">Donation Reader</span>
            </div>

            <#if .main_template_name != "authorize.ftl" && .main_template_name != "setup.ftl" >
                <ul class="nav navbar-nav">
                    <li class="<#if .main_template_name == "realtime.ftl">active </#if> ">
                        <a href="/realtime">RealTime ${.output_format}</a>
                    </li>
                    <li class="<#if .main_template_name == "history.ftl">active </#if> ">
                        <a href="/history">History</a>
                    </li>
                </ul>
            </#if>

            <div class="collapse navbar-collapse">
                <p class="navbar-text navbar-right">(${mode})
                <#--<a href="#" class="navbar-link">TODO</a>-->
                </p>
            </div>

        </div>

    </nav>

    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
                <div class="alert alert-danger <#if !error??> hidden </#if> "
                     role="alert" id="errorAlert">
                    <button onclick="$('#errorAlert').addClass('hidden')" type="button" class="close"
                            aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <p id="errorAlertMessage">${error!""}</p>
                </div>
            </div>
        </div>
    </div>


    <div class="container-fluid">

        <!--begin nested-->
        <#nested/>
        <!--end nested-->
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/js-cookie/2.2.0/js.cookie.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/socket.io/2.0.4/socket.io.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jsrender/0.9.90/jsrender.min.js"></script>
    <script src="${publicAt('js/main.js')}"></script>
</body>

</html>

</#macro>