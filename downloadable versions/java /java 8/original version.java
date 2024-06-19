import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends Application {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int BOX_SIZE = 20;

    private List<Point> snake = new ArrayList<>();
    private Point food;
    private int score = 0;
    private int dx = 0;
    private int dy = 0;

    private GraphicsContext gc;
    private Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        BorderPane root = new BorderPane(canvas);
        root.setBackground(new Background(new BackgroundFill(Color.GREY, null, null)));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();

        initializeGame();
        new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) { // 100 milliseconds
                    update();
                    draw();
                    lastUpdate = now;
                }
            }
        }.start();

        scene.setOnKeyPressed(event -> {
            KeyCode keyPressed = event.getCode();

            if (keyPressed == KeyCode.UP && dy != BOX_SIZE) {
                dx = 0;
                dy = -BOX_SIZE;
            } else if (keyPressed == KeyCode.DOWN && dy != -BOX_SIZE) {
                dx = 0;
                dy = BOX_SIZE;
            } else if (keyPressed == KeyCode.LEFT && dx != BOX_SIZE) {
                dx = -BOX_SIZE;
                dy = 0;
            } else if (keyPressed == KeyCode.RIGHT && dx != -BOX_SIZE) {
                dx = BOX_SIZE;
                dy = 0;
            }
        });
    }

    private void initializeGame() {
        snake.clear();
        snake.add(new Point(200, 200));
        score = 0;
        dx = 0;
        dy = 0;
        placeFood();
    }

    private void placeFood() {
        int x = random.nextInt(WIDTH / BOX_SIZE) * BOX_SIZE;
        int y = random.nextInt(HEIGHT / BOX_SIZE) * BOX_SIZE;
        food = new Point(x, y);
    }

    private void update() {
        moveSnake();
        checkCollision();
    }

    private void moveSnake() {
        Point head = new Point(snake.get(0).getX() + dx, snake.get(0).getY() + dy);
        snake.add(0, head);

        if (head.equals(food)) {
            score++;
            placeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.getX() < 0 || head.getX() >= WIDTH || head.getY() < 0 || head.getY() >= HEIGHT ||
                snake.subList(1, snake.size()).contains(head)) {
            showGameOverAlert();
        }
    }

    private void showGameOverAlert() {
        new Alert(Alert.AlertType.INFORMATION, "Game Over! Your score is " + score).showAndWait();
        initializeGame();
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.GREEN);
        snake.forEach(segment -> gc.fillRect(segment.getX(), segment.getY(), BOX_SIZE, BOX_SIZE));

        gc.setFill(Color.RED);
        gc.fillRect(food.getX(), food.getY(), BOX_SIZE, BOX_SIZE);

        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 10, 30);
    }

    static class Point {
        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }
    }
}
