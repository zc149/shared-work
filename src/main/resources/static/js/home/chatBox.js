let stompClient = null;

function updateChatBox(message, isSender) {
    const chatBox = document.querySelector('.chat-box');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${isSender ? 'sender' : 'receiver'}`;

    // 메시지 내용과 닉네임, 날짜를 포함
    messageDiv.innerHTML = `
        <div class="message-header">
            <strong>${message.writer}</strong>
            <span class="message-time">${new Date(message.date).toLocaleString()}</span>
        </div>
        <div class="message-content">${message.message}</div>
    `;

    chatBox.appendChild(messageDiv);
    chatBox.scrollTop = chatBox.scrollHeight; // 최신 메시지로 스크롤
}


function loadChatMessages() {
    const chattingRoomId = 1; // 해당 채팅방 ID
    fetch(`/chatting/${chattingRoomId}/messages`)
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                const isSender = message.userId === document.getElementById('userId').textContent;
                updateChatBox(message, isSender); // 각 메시지를 채팅박스에 추가
            });
        })
        .catch(error => console.error('Error fetching messages:', error));
}

document.addEventListener('DOMContentLoaded', (event) => {
    const socket = new SockJS('/chatting'); // WebSocket 엔드포인트
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log(frame);

        // /subscription/chatting/1 주제에 구독
        stompClient.subscribe('/subscription/chatting/1', function(message) {
            const data = JSON.parse(message.body);
            updateChatBox(data); // 새로운 메시지를 채팅박스에 추가
        });

        loadChatMessages(); // 페이지 로드 시 기존 메시지 불러오기
    }, function(error) {
        console.error('Connection error:', error);
    });
});

document.getElementById('sendButton').addEventListener('click', function() {
    const input = document.getElementById('messageInput');
    const userID = document.getElementById('userId').textContent;
    const userNickName = document.getElementById('userNickName').textContent;
    const messageText = input.value.trim();

    if (messageText) {
        const message = {
            userId: userID,
            writer: userNickName,
            message: messageText
        };
        stompClient.send('/publication/chatting/1', {}, JSON.stringify(message)); // chattingRoomId를 1로 설정
        input.value = '';
    }
});
