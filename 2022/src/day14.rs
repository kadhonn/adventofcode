use crate::day14::Tile::{Air, Rock, Sand};

#[derive(Copy, Clone)]
enum Tile {
    Air,
    Sand,
    Rock,
}

pub fn day14_1(str: &str) {
    let mut field = parse_field(str);

    // print_field(&field);
    let mut count = 0;
    'outer: loop {
        let mut x = 500;
        let mut y = 0;
        loop {
            if y + 1 >= field.len() {
                break 'outer;
            }
            if let Air = field[y + 1][x] {
                y += 1;
            } else if let Air = field[y + 1][x - 1] {
                y += 1;
                x -= 1;
            } else if let Air = field[y + 1][x + 1] {
                y += 1;
                x += 1;
            } else {
                break;
            }
        }
        field[y][x] = Sand;
        count += 1;
    }
    println!("{count}");
}

pub fn day14_2(str: &str) {
    let mut field = parse_field2(str);

    // print_field(&field);
    let mut count = 0;
    loop {
        let mut x = 500;
        let mut y = 0;
        loop {
            if let Air = field[y + 1][x] {
                y += 1;
            } else if let Air = field[y + 1][x - 1] {
                y += 1;
                x -= 1;
            } else if let Air = field[y + 1][x + 1] {
                y += 1;
                x += 1;
            } else {
                break;
            }
        }
        field[y][x] = Sand;
        count += 1;

        if y == 0 && x == 500 {
            break;
        }
    }
    println!("{count}");
}

fn print_field(field: &Vec<Vec<Tile>>) {
    for line in field {
        for tile in line {
            print!("{}", match tile {
                Air => ".",
                Tile::Sand => "O",
                Rock => "#"
            })
        }
        println!();
    }
    println!();
}

fn parse_field2(str: &str) -> Vec<Vec<Tile>> {
    let mut field = vec![vec![Air; 1000]; 1000];

    let mut max_y = 0;
    for line in str.lines() {
        let mut steps = line.split(" -> ");
        let mut last_step = parse_step(steps.next().unwrap());
        loop {
            max_y = max_y.max(last_step.1);
            let next_step = steps.next();
            if let None = next_step {
                break;
            }
            let next_step = next_step.unwrap();
            let next_step = parse_step(next_step);

            let mut x = last_step.0;
            let mut y = last_step.1;
            let x_dir = (next_step.0 - last_step.0).signum();
            let y_dir = (next_step.1 - last_step.1).signum();

            while x != next_step.0 || y != next_step.1 {
                field[y as usize][x as usize] = Rock;

                x += x_dir;
                y += y_dir;
            }
            field[y as usize][x as usize] = Rock;

            last_step = next_step;
        }
    }

    for x in 0..1000 {
        field[(max_y + 2) as usize][x] = Rock;
    }

    field
}

fn parse_field(str: &str) -> Vec<Vec<Tile>> {
    let mut field = vec![vec![Air; 1000]; 1000];

    for line in str.lines() {
        let mut steps = line.split(" -> ");
        let mut last_step = parse_step(steps.next().unwrap());
        loop {
            let next_step = steps.next();
            if let None = next_step {
                break;
            }
            let next_step = next_step.unwrap();
            let next_step = parse_step(next_step);

            let mut x = last_step.0;
            let mut y = last_step.1;
            let x_dir = (next_step.0 - last_step.0).signum();
            let y_dir = (next_step.1 - last_step.1).signum();

            while x != next_step.0 || y != next_step.1 {
                field[y as usize][x as usize] = Rock;

                x += x_dir;
                y += y_dir;
            }
            field[y as usize][x as usize] = Rock;

            last_step = next_step;
        }
    }

    field
}

fn parse_step(step: &str) -> (i32, i32) {
    let mut split = step.split(",");
    return (split.next().unwrap().parse().unwrap(), split.next().unwrap().parse().unwrap());
}
