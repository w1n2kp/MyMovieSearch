<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Movie Search Demo with Lucene & J2EE</title>
  <script type="text/javascript" src="js/jquery.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      $("#btnSearch").click(function() {
        $("#display").load("search.do?q=" + $("#keyword").val(), function(data, status){});
      });

      $("#btnIndex").click(function() {
        $("#display").innerText="Wait a second ...";
        $("#display").load("index.do", function(data, status){});
      });

      $("#btnImport").click(function() {
        window.open("import.do","_blank","");
      });
    });

  </script>
</head>
<body>
<h1>Movie Search Demo with Lucene & J2EE</h1>
<input id="keyword" value="变形金刚" type="text"></input>
<input type="button" value="搜索" id="btnSearch"></input>
<input type="button" value="重建索引" id="btnIndex">
<input type="button" value="导入Mysql" id="btnImport"></input>
<div id="display"></div>
</body>
</html>