<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>pedidosApp</title>
    <!-- CSS only -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
    <!-- JavaScript Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
    <link href="style.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=JetBrains+Mono:ital,wght@0,100;0,300;0,400;1,100;1,300;1,400&display=swap" rel="stylesheet">
    <link href="login/login.css" rel="stylesheet">
</head>
<body>
    <!-- Barra de bienvenida -->
    <div class="nav bg-primary">
        <h1 id="welcome" >Bienvenido!</h1>
    </div>

    
    <div class="container-fluid align-items-center text-center">
        <!-- Titulo de la app -->
        <div class="row">
            <h1 id="title" class="my-5">pedidosApp</h1>
        </div>

        <!-- Logo -->
        <div class="row">
            <img src="resources/logo.png" alt="logo img">
        </div>
            

        <!-- Login button -->
        <form action="login/login.php" method="post"> 
            <input type="submit" class="btn btn-primary btn-lg" value="Iniciar Sesion">
        </form>

    </div>
</body>
</html>