export function toggleIcon() {
  const toggler = document.getElementsByClassName('prev_caret');
  let i;
  for (i = 0; i < toggler.length; i++) {
    toggler[i].addEventListener('click', function () {
      this.parentElement.querySelector('.prev_nested').classList.toggle('active');
      this.classList.toggle('prev_caret-down');
    });
  }
}
