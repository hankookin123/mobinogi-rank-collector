<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>랭킹 데이터</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <h2>랭킹 데이터 리스트</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>날짜</th>
                <th>서버ID</th>
                <th>직업ID</th>
                <th>1등</th>
                <th>1000등</th>
                <th>500등</th>
                <th>평균</th>
                <th>총합</th>
                <th>분산</th>
                <th>표준편차</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${daily_power}">
                <tr>
                    <td>${item.id}</td>
                    <td>${item.dataDate}</td>
                    <td>${item.serverId}</td>
                    <td>${item.jobId}</td>
                    <td>${item.powerTop}</td>
                    <td>${item.powerBottom}</td>
                    <td>${item.powerMedian}</td>
                    <td>${item.powerAverage}</td>
                    <td>${item.powerTotal}</td>
                    <td>${item.powerVar}</td>
                    <td>${item.powerSd}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <!-- 그래프 영역 -->
    <h3>평균값 추이 그래프</h3>
    <canvas id="averageChart" width="800" height="400"></canvas>

    <script>
    // 데이터 추출 (JSP → JS)
    const labels = [
        <c:forEach var="item" items="${daily_power}" varStatus="status">
            "${item.dataDate}"<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
    const averages = [
        <c:forEach var="item" items="${daily_power}" varStatus="status">
            ${item.powerAverage}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Chart.js로 그래프 그리기
    const ctx = document.getElementById('averageChart').getContext('2d');
    const averageChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: '평균',
                data: averages,
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                fill: false,
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: { title: { display: true, text: '날짜' } },
                y: { title: { display: true, text: '평균값' } }
            }
        }
    });
    </script>
    
</body>
</html>
