<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
        <?php
         $idP= $_GET["idP"];
         
      echo '<form style="display:none;" class="cart_form" action="../default.asp?cmd=showCart" method="post" name="cart_'. $idP.'" id="cart_'. $idP.'" onsubmit="return sendData(this);">
                <input type="text" name="addToCart" value="1" size="3" maxlength="6">
                <input value="Aggiungi" type="submit" name="cmdAdd" class="butt1">
                <input value="'. $idP.'" type="hidden" name="productID">
                <input type="hidden" value="" name="filterID">
            </form>
            <div> Caricamento in corso !! Prodotto '. $idP.' </div>
            <script language="javascript" type="text/javascript">
                document.forms["cart_'. $idP.'"].submit()
            </script>';
      //setTimeout("document.getElementById('test').submit()", 3000)
      //document.forms["cart"].submit()
        ?>
    </body>
</html>

