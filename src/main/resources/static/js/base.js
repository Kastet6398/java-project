$(() => {
    function loadTheme() {
        if (!localStorage.getItem("light")) {
            $(".bg-dark").attr("bg-light", true);
            $(".bg-dark").removeClass("bg-dark");

            $(".text-white").attr("text-black", true);
            $(".text-white").removeClass("text-white");
        } else {
            $("[bg-light]").addClass("bg-dark");
            $("[bg-light]").removeAttr("bg-light");

            $("[text-black]").addClass("text-white");
            $("[text-black]").removeAttr("text-black");
        }
    }


    $('#dark-mode').on('click', function (e) {

        e.preventDefault();
        if (!localStorage.getItem("light")) {
            localStorage.setItem("light", true);
        } else {
            localStorage.removeItem("light");
        }
    });
    setInterval(loadTheme, 10);
});