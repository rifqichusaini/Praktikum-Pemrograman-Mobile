<?php 

$host = "sql100.infinityfree.com";
$user = "if0_40604648";
$pass = "Qq30092005";
$db   = "if0_40604648_prak6_pmob";

$connection = mysqli_connect($host, $user, $pass, $db);

//query ke table Mahasiswa
$sql = "select * from Mahasiswa";
$result = mysqli_query($connection, $sql);

//pembuatan array
$emparray = array();
while($row = mysqli_fetch_assoc($result))
{
    $emparray[] = $row;
}

//pembuatan json
echo json_encode($emparray);

//tutup koneksi
mysqli_close($connection);

?>
