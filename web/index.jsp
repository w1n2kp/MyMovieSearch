<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Movie Search Demo with Lucene & J2EE</title>
    <style>
        .title {
            font-size: 15pt;
            font-weight: bold
        }

        .org_title {
            font-size: 10pt;
            color: gray
        }

        .thumb_container {
            width: auto;
            text-align: center
        }

        h2 {
            font-size: 15pt;
            font-weight: bold
        }

        .thumb_container {
            width: 200px;
        }

        .poster_thumb {
            height: 220px;
            width: 150px
        }

        span {
            font-size: 10pt;
            color: gray;
        }

    </style>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#btnSearch").click(function () {
                $.getJSON("search.do?q=" + $("#keyword").val(), function (data, status) {
                    var code = "<table id='container'>";
                    for (var n = 0; n < data.length; n++) {
                        i = data[n];

                        code += "<tr><td class='thumb_container'><img class='poster_thumb' src='poster.do?id=" + i.id + "'></td><td>" +
                        "<table>" +
                        "<tr><td><span class='title'>" + i.title + "</span> / <span class='org_title'>" + i.originalTitle + "</span> <span class='year'>" + i.year + "</span></td></tr>" +
                        "<tr><td>类别：<span class='genres'>" + i.genres + "</span>" +
                        " / 导演：<span class='directors'>" + i.directors + "</span>" +
                        " / 演员：<span class='actors'>" + i.actors + "</span>" +
                        " / 国家：<span class='countries'>" + i.countries + "</span></td></tr>" +
                        "<tr><td class='plot' colspan='2'>" + i.plot + "</td></tr>" +
                        "</table>" +
                        "</td></tr>";


                    }

                    $("#display").html(code + "</table>");
                });
            });

            $("#btnIndex").click(function () {
                $("#display").innerText = "Wait a second ...";
                $("#display").load("index.do", function (data, status) {
                });
            });

            $("#btnImport").click(function () {
                window.open("import.do", "_blank", "");
            });

            $("#btnUpload").click(function() {
               window.location.href = "upload.jsp";
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
<input type="button" value="上传解析文档" id="btnUpload"></input>

<div id="display"></div>
</body>
</html>