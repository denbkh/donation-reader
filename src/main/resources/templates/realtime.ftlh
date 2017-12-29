<#import "layout.ftlh" as layout>

<@layout.siteLayout>
<div class="row">
    <div class="col-sm-12">
        <h4>Вывод информации о донейтах в реальном времени</h4>
        <div class="form-inline">


            <div class="form-group">
                <label for="moneyFilter" class="control-label">Мин. сумма для отображения:</label>

                <div class="input-group">
                    <span class="input-group-addon">RUB</span>
                    <input type="text" class="form-control" aria-label="Сумма" id="moneyFilter" value="2000">
                </div>
            </div>

            <button type="button" class="btn btn-primary" id="startRealTimeButton"
                    data-loading-text="<i class='fa fa-spinner fa-spin '></i> Запускаем ..."
                    onclick="startRealTime('${csrfToken}');">
                Запустить
            </button>

            <button type="button" class="btn btn-danger hidden" id="stopRealTimeButton"
                    onclick="stopRealTime();">
                Остановить
            </button>

        </div>

        <div class="alert alert-success hidden" id="connectedAlert">
            Подключение к streamlabs.com установлено! Все донейты больше <span
                id="connectedAlertMoneyVal">TODO</span> руб. будут выведены в таблице в реальном времени.
        </div>

        <table class="table table-hover">
            <thead>
            <tr>
                <th>Время</th>
                <th>Имя</th>
                <th>Сумма</th>
                <th>Коммент</th>
            </tr>
            </thead>
            <tbody id="donationsTable">

            </tbody>
        </table>


    </div>
</div>

<script id="rowTemplate" type="text/x-jsrender">
    <tr>
        <td>{{:date}}</td>
        <td>{{:name}}</td>
        <td>{{:amount}}</td>
        <td>{{:message}}</td>
    </tr>
</script>

</@layout.siteLayout>