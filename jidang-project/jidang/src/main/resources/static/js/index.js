document.addEventListener("DOMContentLoaded", () => {

    // 🔍 검색창 토글
    window.toggleSearch = function () {
        const form = document.getElementById("searchForm");
        if (!form) return;

        form.classList.toggle("hidden");

        // 검색창 열리면 포커스 자동 이동
        if (!form.classList.contains("hidden")) {
            const input = form.querySelector('input[name="keyword"]');
            if (input) input.focus();
        }
    };

    // ☰ 모바일 메뉴 토글
    window.toggleMobileMenu = function () {
        const menu = document.getElementById("mobileMenu");
        if (menu) {
            menu.classList.toggle("hidden");
        }
    };

});
