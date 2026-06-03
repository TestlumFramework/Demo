const input = document.querySelector('input[placeholder="Input 1"]');

let count = parseInt(input.value);
if (isNaN(count)) {
    count = 0;
}

input.value = count + 1;