import pygame
import sys
import random

# Initialize Pygame
pygame.init()

# Set up the screen
WIDTH, HEIGHT = 800, 600
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("^($@)")

# Set up colors
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
RED = (255, 0, 0)

# Set up the player
player_size = 50
player_pos = [WIDTH // 2, HEIGHT - 2 * player_size]
player_speed = 10

# Set up the enemy
enemy_size = 50
enemy_pos = [random.randint(0, WIDTH - enemy_size), 0]
enemy_list = [enemy_pos]
enemy_speed = 5

# Set up the bullet
bullet_size = 10
bullet_pos = [0, 0]
bullet_list = []
bullet_speed = 10

# Set up game variables
score = 0
game_over = False

# Set up clock
clock = pygame.time.Clock()

# Set up fonts
font = pygame.font.SysFont(None, 50)

# Function to draw objects
def draw_objects():
    screen.fill(BLACK)
    for pos in enemy_list:
        pygame.draw.rect(screen, RED, (pos[0], pos[1], enemy_size, enemy_size))
    pygame.draw.rect(screen, WHITE, (player_pos[0], player_pos[1], player_size, player_size))
    for pos in bullet_list:
        pygame.draw.rect(screen, WHITE, (pos[0], pos[1], bullet_size, bullet_size))

# Function to update enemy position
def update_enemy_position():
    global score, game_over
    for index, pos in enumerate(enemy_list):
        if pos[1] >= 0 and pos[1] < HEIGHT:
            pos[1] += enemy_speed
        else:
            enemy_list.pop(index)
            score += 1
    if player_collision():
        game_over = True

# Function to generate enemies
def generate_enemies():
    delay = random.random()
    if len(enemy_list) < 10 and delay < 0.1:
        x_pos = random.randint(0, WIDTH - enemy_size)
        y_pos = 0
        enemy_list.append([x_pos, y_pos])

# Function to update bullet position
def update_bullet_position():
    for index, pos in enumerate(bullet_list):
        if pos[1] > 0:
            pos[1] -= bullet_speed
        else:
            bullet_list.pop(index)

# Function to check collision between player and enemy
def player_collision():
    for pos in enemy_list:
        if detect_collision(player_pos, pos):
            return True
    return False

# Function to check collision between objects
def detect_collision(player_pos, enemy_pos):
    p_x = player_pos[0]
    p_y = player_pos[1]

    e_x = enemy_pos[0]
    e_y = enemy_pos[1]

    if (e_x >= p_x and e_x < (p_x + player_size)) or (p_x >= e_x and p_x < (e_x + enemy_size)):
        if (e_y >= p_y and e_y < (p_y + player_size)) or (p_y >= e_y and p_y < (e_y + enemy_size)):
            return True
    return False

# Main game loop
while not game_over:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()
        elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_LEFT:
                player_pos[0] -= player_speed
            elif event.key == pygame.K_RIGHT:
                player_pos[0] += player_speed
            elif event.key == pygame.K_SPACE:
                bullet_pos[0] = player_pos[0] + player_size // 2 - bullet_size // 2
                bullet_pos[1] = player_pos[1]
                bullet_list.append(list(bullet_pos))

    # Update objects
    update_enemy_position()
    generate_enemies()
    update_bullet_position()

    # Draw objects
    draw_objects()

    # Display score
    score_text = "Score: " + str(score)
    score_display = font.render(score_text, True, WHITE)
    screen.blit(score_display, (10, 10))

    # Update the display
    pygame.display.update()

    # Set the FPS
    clock.tick(50)
