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
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <a class="navbar-brand" href="#">Donation Reader</a>
            </div>
        </div><!-- /.container-fluid -->
    </nav>

    <div class="container-fluid">

        <div class="alert alert-danger alert-dismissible hidden" role="alert" id="error">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">&times;</span></button>
            <p id="errorDescription"></p>
        </div>


        <div class="row" id="authForm">
            <div class="col-sm-6">
                <button class="btn btn-primary" onclick="authorizeToStreamLabs()">Авторизовать в streamlabs.com</button>
            </div>
        </div>


        <div class="row hidden">
            <div class="col-sm-6">
                <div class="form-horizontal">

                    <div class="form-group">
                        <label for="startDate" class="col-sm-4 control-label">Время начала:</label>

                        <div class="col-sm-5">
                            <div class='input-group date' id='datetimepicker2'>
                                <input type='text' class="form-control" id="startDate"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="moneyFilter" class="col-sm-4 control-label">Мин. сумма:</label>

                        <div class="col-sm-5">
                            <input type="text" class="form-control" id="moneyFilter" value="2000">
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.20.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/dexie/2.0.1/dexie.min.js"></script>
    <script src="${publicAt('js/main.js')}"></script>
</body>

</html>