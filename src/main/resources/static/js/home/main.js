$(function () {

    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 변환

    sendDate(formattedDate); // 처음 렌더링시 오늘 날짜


    rome(inline_cal, { time: false, inputFormat: 'YYYY-MM-DD' }).on('data', function (value) {
        sendDate(value);
    });

    function sendDate(date) {
        $.ajax({
            url: '/todolist',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ todoDate: date }),
            success: function (response) {
                console.log('성공:');
                updateTodoList(response);  // 받은 데이터를 화면에 반영
            },
            error: function (xhr, status, error) {
                console.error('실패:', error);
            }
        });
    }

    function updateTodoList(todoList) {
        var tbody = $('.table tbody');
        tbody.empty();  // 기존 데이터 비우기

        if (todoList.length === 0) {
            tbody.append('<tr><td>목록이 없습니다.</td></tr>');  // 비어있을 경우 메시지
        } else {
            todoList.forEach(function (todo) {
                tbody.append('<tr><td><span>' + todo.content + '</span></td></tr>');  // 리스트 추가
            });
        }
    }
});
