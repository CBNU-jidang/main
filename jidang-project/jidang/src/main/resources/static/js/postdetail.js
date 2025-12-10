document.addEventListener("DOMContentLoaded", () => {
    // 좋아요 버튼
    const likeButton = document.getElementById("likeButton");
    if (likeButton) {
        likeButton.addEventListener("click", () => {
            const url = likeButton.dataset.likeUrl;
            if (url) window.location.href = url;
        });
    }

    // 댓글 버튼 → 댓글 입력 창 포커스
    const commentButton = document.getElementById("commentButton");
    if (commentButton) {
        commentButton.addEventListener("click", () => {
            const textarea = document.querySelector(".comment-textarea");
            if (textarea) textarea.focus();
        });
    }

    // 공유 버튼
    const shareButton = document.getElementById("shareButton");
    if (shareButton) {
        shareButton.addEventListener("click", () => {
            alert("공유 기능은 추후 추가될 예정입니다.");
        });
    }
});

/* ============================================
   대댓글 폼 토글
   ============================================ */
function toggleReplyForm(button) {

    // 클릭한 버튼이 속한 comment-item을 찾는다.
    const commentItem = button.closest(".comment-item");
    if (!commentItem) return;

    // comment-item 내부의 reply-form을 찾는다.
    const replyForm = commentItem.querySelector(".reply-form");
    if (!replyForm) return;

    // 표시/숨김 토글
    if (replyForm.style.display === "none" || replyForm.style.display === "") {
        replyForm.style.display = "flex";  // comment-form이 flex 사용하므로 flex로 열기
    } else {
        replyForm.style.display = "none";
    }
}
