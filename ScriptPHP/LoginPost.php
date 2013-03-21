<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
        <?php
         
         
      echo '<form style="display:none;" action="../default.asp" method="post" name="login" id="login">
		<label for="uid">User:</label>
		<input type="text" size="10" value="gianluca86" id="uid" name="uid">
		<label for="pwd">Password:</label>
		<input type="password" size="10" value="spinco" id="pwd" name="pwd">
		<label for="remember" style="font-size:10px;width:83%;">Ricorda password</label>
		<input type="checkbox" id="remember" name="remember" style="background:#fff;width:auto;float:right;">
		<input type="submit" value="Accedi" class="butt1" style="float:right;clear:both;margin-bottom:2px;">
	</form>
            <div> Caricamento !! </div>
            <script language="javascript" type="text/javascript">
                document.forms["login"].submit()
            </script>';
        ?>
    </body>
</html>