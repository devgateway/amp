export function toggleIcon(cssSelector) {
  const toggler = document.getElementsByClassName(cssSelector);
  console.log(cssSelector);
  console.log(toggler);
  let i;
  for (i = 0; i < toggler.length; i++) {
    if (toggler[i].getAttribute('listener') !== 'true') {
      toggler[i].setAttribute('listener', 'true');
      toggler[i].addEventListener('click', function () {
        debugger
        this.parentElement.querySelector('.prev_nested').classList.toggle('active');
        this.classList.toggle('prev_caret-down');
      });
    }
  }
}
