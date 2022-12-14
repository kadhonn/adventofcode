use std::ops::Range;

pub fn day8_1(str: &&str) {
    let field: Vec<Vec<i32>> = parse_field(str);
    let mut visible_matrix: Vec<Vec<bool>> = vec![vec![false; field.len()]; field[0].len()];
    let mut count = 0;

    for y in 0..field.len() {
        for x in 0..field[y].len() {
            visible_matrix[y][x] = check_field(y, x, &field);
            if visible_matrix[y][x] {
                count += 1;
            }
        }
    }

    println!("{count}");
}

fn check_field(y: usize, x: usize, field: &Vec<Vec<i32>>) -> bool {
    if x == 0 || x == field[0].len() - 1 || y == 0 || y == field.len() {
        return true;
    }
    let val = field[y][x];

    let visible =
        check_range(y..y + 1, 0..x, val, field)
            || check_range(y..y + 1, x + 1..field[0].len(), val, field)
            || check_range(0..y, x..x + 1, val, field)
            || check_range(y + 1..field.len(), x..x + 1, val, field);

    return visible;
}

fn check_range(y_range: Range<usize>, x_range: Range<usize>, val: i32, field: &Vec<Vec<i32>>) -> bool {
    for y in y_range {
        for x in x_range.clone() {
            if field[y][x] >= val {
                return false;
            }
        }
    }
    return true;
}

pub fn day8_2(str: &&str) {
    let field: Vec<Vec<i32>> = parse_field(str);
    let mut max_score = 0;

    check_field_2(2, 1, &field);
    for y in 0..field.len() {
        for x in 0..field[y].len() {
            let score = check_field_2(y, x, &field);
            max_score = max_score.max(score);
        }
    }

    println!("{max_score}");
}

fn check_field_2(y: usize, x: usize, field: &Vec<Vec<i32>>) -> i32 {
    let val = field[y][x];

    let score1 = check_range_2(y..(y + 1), (0..x).rev(), val, field);
    let score2 = check_range_2(y..(y + 1), (x + 1)..field[0].len(), val, field);
    let score3 = check_range_2((0..y).rev(), x..(x + 1), val, field);
    let score4 = check_range_2((y + 1)..field.len(), x..(x + 1), val, field);

    // println!("{score1} {score2} {score3} {score4}\n\n");

    return score1 * score2 * score3 * score4;
}

fn check_range_2<I, T>(y_range: I, x_range: T, val: i32, field: &Vec<Vec<i32>>) -> i32
    where
        I: Iterator<Item=usize> + Clone,
        T: Iterator<Item=usize> + Clone {
    let mut count = 0;
    'outer: for y in y_range {
        for x in x_range.clone() {
            count += 1;
            // println!("{} {} {} {}", val, field[y][x], x,y);
            if field[y][x] >= val {
                break 'outer;
            }
        }
    }
    // println!();
    return count;
}

fn parse_field(str: &&str) -> Vec<Vec<i32>> {
    str.split("\r\n").map(|line| {
        line.chars().map(|char| { char.to_digit(10).unwrap() as i32 }).collect()
    }).collect()
}