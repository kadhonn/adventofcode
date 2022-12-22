pub fn day22_1(str: &str) {
    let (field, movements) = parse_input(str);

    let mut position = (0, 0);
    let mut direction = (1, 0);
    for x in 0..field[0].len() {
        if field[0][x] == '.' {
            position = (x as i32, 0);
            break;
        }
    }

    for movement in movements {
        match movement {
            Movement::Left => { direction = turn_left(direction); }
            Movement::Right => { direction = turn_right(direction); }
            Movement::Steps(steps) => { position = move_steps(&field, position, direction, steps); }
        }
    }

    let result = 1000 * (position.1 + 1) + 4 * (position.0 + 1) + match direction {
        (1, 0) => 0,
        (0, 1) => 1,
        (-1, 0) => 2,
        (0, -1) => 3,
        _ => panic!("invalid direction! {:?}", direction)
    };

    println!("{}", result);
}

fn move_steps(field: &Vec<Vec<char>>, position: (i32, i32), direction: (i32, i32), steps: i32) -> (i32, i32) {
    let mut new_pos = position;
    'outer: for _ in 0..steps {
        let mut pos = new_pos;
        loop {
            pos = ((pos.0 + direction.0 + field[pos.1 as usize].len() as i32) % field[pos.1 as usize].len() as i32, (pos.1 + direction.1 + field.len() as i32) % field.len() as i32);
            if field[pos.1 as usize][pos.0 as usize] == '.' {
                break;
            } else if field[pos.1 as usize][pos.0 as usize] == '#' {
                break 'outer;
            }
        }
        new_pos = pos;
    }
    new_pos
}

fn turn_left(direction: (i32, i32)) -> (i32, i32) {
    return (direction.1, -direction.0);
}

fn turn_right(direction: (i32, i32)) -> (i32, i32) {
    return (-direction.1, direction.0);
}

fn parse_input(str: &str) -> (Vec<Vec<char>>, Vec<Movement>) {
    let mut lines = str.lines();

    let mut field: Vec<Vec<char>> = vec![];

    loop {
        let line = lines.next().unwrap();
        if line.is_empty() {
            break;
        }
        field.push(line.chars().collect())
    }

    let mut max_cols = 0;
    for line in &field {
        max_cols = max_cols.max(line.len())
    }

    for line in &mut field {
        for _ in 0..(max_cols - line.len()) {
            line.push(' ');
        }
    }

    let mut movements = vec![];
    let chars = lines.next().unwrap().chars();
    let mut buf = String::new();
    for char in chars {
        if char.is_digit(10) {
            buf.push(char);
        } else {
            if !buf.is_empty() {
                movements.push(Movement::Steps(buf.parse().unwrap()));
                buf.clear();
            }
            match char {
                'L' => movements.push(Movement::Left),
                'R' => movements.push(Movement::Right),
                _ => { panic!("invalid char {}", char); }
            }
        }
    }
    if !buf.is_empty() {
        movements.push(Movement::Steps(buf.parse().unwrap()));
        buf.clear();
    }

    (field, movements)
}

#[derive(Debug)]
enum Movement {
    Steps(i32),
    Left,
    Right,
}