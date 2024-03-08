class Spotlight {
	constructor(containerElement) {
		this.container = containerElement;
		this.cards = Array.from(this.container.children);
		this.mouse = {
			x: 0,
			y: 0,
		};
		this.containerSize = {
			w: 0,
			h: 0,
		};
		this.initContainer = this.initContainer.bind(this);
		this.onMouseMove = this.onMouseMove.bind(this);
		this.init();
	}

	initContainer() {
		this.containerSize.w = this.container.offsetWidth;
		this.containerSize.h = this.container.offsetHeight;
	}

	onMouseMove(event) {
		const { clientX, clientY } = event;
		const rect = this.container.getBoundingClientRect();
		const { w, h } = this.containerSize;
		const x = clientX - rect.left;
		const y = clientY - rect.top;
		const inside = x < w && x > 0 && y < h && y > 0;
		if (inside) {
			this.mouse.x = x;
			this.mouse.y = y;
			this.cards.forEach((card) => {
				const cardX = -(card.getBoundingClientRect().left - rect.left) + this.mouse.x;
				const cardY = -(card.getBoundingClientRect().top - rect.top) + this.mouse.y;
				card.style.setProperty('--mouse-x', `${cardX}px`);
				card.style.setProperty('--mouse-y', `${cardY}px`);
			});
		}
	}

	init() {
		this.initContainer();
		window.addEventListener('resize', this.initContainer);
		window.addEventListener('mousemove', this.onMouseMove);
	}
}

const spotlights = document.querySelectorAll('[data-spotlight]');
spotlights.forEach((spotlight) => {
	new Spotlight(spotlight);
});
document.addEventListener('DOMContentLoaded', function () {
	gsap.registerPlugin(ScrollTrigger);

	gsap.from(".dx-fixed-background__media-wrapper", {
		scale: 0.55,
		scrollTrigger: {
			trigger: ".dx-fixed-background__media-wrapper",
			start: "top bottom",
			end: "center 75%",
			scrub: true
		}
	});
	gsap.from(".dx-fixed-background__media", {
		borderRadius: "300px",
		scrollTrigger: {
			trigger: ".dx-fixed-background__media-wrapper",
			start: "top bottom",
			end: "center 75%",
			scrub: true
		}
	});
	document.addEventListener("DOMContentLoaded", function () {
        var myButton = document.getElementById("myButton");

        myButton.addEventListener("click", function (event) {
            event.preventDefault();
            var nextPageURL = "NEW.html";
            window.location.href = nextPageURL;
        });
    });

});