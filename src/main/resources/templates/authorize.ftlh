<#--<#global hideNavBar=true>-->
<#import "layout.ftlh" as layout>

<@layout.siteLayout>
<div class="row" id="authForm">
    <div class="col-sm-12">
        <p>Для получения информации о ваших донейтах данному сайту нужно разрешение от streamlabs. Права запрашиваются
            только на чтение.</p>
        <a class="btn btn-primary"
           href="${authorizeUrl}"
           role="button">Авторизовать в streamlabs.com</a>
    </div>
</div>
</@layout.siteLayout>