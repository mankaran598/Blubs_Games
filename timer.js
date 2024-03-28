let timeInterval;
const alarmAudio = document.getElementById("alarmAudio");

function startTimer(duration) {
  const minutes = parseInt(duration / 60, 10);
  const seconds = parseInt(duration % 60, 10);
  let timerText = `${minutes.toString().padStart(2, "0")}:${seconds
    .toString()
    .padStart(2, "0")}`;
  document.getElementById("timerText").innerHTML = timerText;
  const timer = duration,
    minutes = parseInt(timer / 60, 10),
    seconds = parseInt(timer % 60, 10);

  timeInterval = setInterval(() => {
    minutes = parseInt(timer / 60, 10);
    seconds = parseInt(timer % 60, 10);

    if (timer > 0) {
      --timer;
      timerText = `${minutes.toString().padStart(2, "0")}:${seconds
        .toString()
        .padStart(2, "0")}`;
      document.getElementById("timerText").innerHTML = timerText;
    } else {
      alarmAudio.play();
    }
  }, 1000);
}

startTimer(5
