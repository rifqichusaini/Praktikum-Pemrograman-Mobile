<?php 

$host = "sql113.infinityfree.com";
$user = "if0_40530819";
$pass = "DeoIkEkPiB";
$db = "if0_40530819_db_employee";

$connection = mysqli_connect($host, $user, $pass, $db);

//query ke table employee
$sql = "select * from tbl_employee";
$result = mysqli_query($connection, $sql);

//pembuatan array
$emparray = array();
while($row =mysqli_fetch_assoc($result))
{
$emparray[] = $row;
}

//pembuatan json
echo json_encode($emparray);

//tutup koneksi
mysqli_close($connection);

?>