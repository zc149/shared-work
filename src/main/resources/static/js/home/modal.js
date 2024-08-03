
// 버튼 클릭 시 모달 열기
document.getElementById('addButton').addEventListener('click', function () {
    document.getElementById('myModal').style.display = 'block';
});

// 추가 버튼 클릭 시 할 일 추가
document.getElementById('addTodoButton').addEventListener('click', function () {
    const todoInput = document.getElementById('todoInput').value;
    if (todoInput) {
        // 여기에 할 일을 추가하는 로직을 구현하세요.
        alert('추가된 할 일: ' + todoInput);
        document.getElementById('todoInput').value = ''; // 입력 필드 초기화
        document.getElementById('myModal').style.display = 'none'; // 모달 닫기
    } else {
        alert('할 일을 입력해 주세요.');
    }
});

// 취소 버튼 클릭 시 모달 닫기
document.getElementById('cancelButton').addEventListener('click', function () {
    document.getElementById('myModal').style.display = 'none';
});

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
    const modal = document.getElementById('myModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};
