use std::collections::{HashSet};

pub fn day12_1(str: &str) {
    let field: Vec<Vec<char>> = str.lines().map(|l| l.chars().collect()).collect();


    let mut start = None;
    let mut end = None;
    for y in 0..field.len() {
        for x in 0..field[y].len() {
            if field[y][x] == 'S' {
                start = Some((x, y));
            }
            if field[y][x] == 'E' {
                end = Some((x, y));
            }
        }
    }


    let field: Vec<Vec<i32>> = str.lines().map(|l| l.chars().map(|c| if c == 'S' { 0 } else if c == 'E' { 'z' as i32 - 'a' as i32 } else { c as i32 - 'a' as i32 }).collect()).collect();
    let start = start.unwrap();
    let end = end.unwrap();
    let mut visited_fields = HashSet::new();

    let mut step_count = 0;
    let mut to_visit = HashSet::new();
    to_visit.insert(start);
    'outer: loop {
        let mut new_to_visit: HashSet<(usize, usize)> = HashSet::new();

        if to_visit.is_empty() {
            panic!("to_visit is empty!");
        }
        for step in to_visit {
            // println!("visiting x: {} y: {}", step.0, step.1);
            if visited_fields.contains(&step) {
                continue;
            }
            visited_fields.insert(step);
            if step == end {
                break 'outer;
            }
            for dir in [(0, 1), (1, 0), (-1, 0), (0, -1)] {
                let next_step = (step.0 as i32 + dir.0, step.1 as i32 + dir.1);
                if next_step.0 >= 0 && next_step.0 < field[0].len() as i32 && next_step.1 >= 0 && next_step.1 < field.len() as i32 {
                    let cur_elevation = field[step.1][step.0];
                    let next_elevation = field[next_step.1 as usize][next_step.0 as usize];

                    if next_elevation <= cur_elevation + 1 {
                        let next_step = (next_step.0 as usize, next_step.1 as usize);
                        if !visited_fields.contains(&next_step) {
                            new_to_visit.insert(next_step);
                        }
                    }
                }
            }
        }

        to_visit = new_to_visit;
        step_count += 1;
    }

    println!("{step_count}");
}

pub fn day12_2(str: &str) {
    let field: Vec<Vec<char>> = str.lines().map(|l| l.chars().collect()).collect();

    let mut start = vec!();
    let mut end = None;
    for y in 0..field.len() {
        for x in 0..field[y].len() {
            if field[y][x] == 'S' || field[y][x] == 'a' {
                start.push((x, y));
            }
            if field[y][x] == 'E' {
                end = Some((x, y));
            }
        }
    }

    let field: Vec<Vec<i32>> = str.lines().map(|l| l.chars().map(|c| if c == 'S' { 0 } else if c == 'E' { 'z' as i32 - 'a' as i32 } else { c as i32 - 'a' as i32 }).collect()).collect();
    let start = start;
    let end = end.unwrap();

    let mut min = 10000000;
    for start in start {
        min = min.min(try_from(start, end, &field));
    }

    println!("{min}");
}

fn try_from(start: (usize, usize), end: (usize, usize), field: &Vec<Vec<i32>>) -> i32 {
    let mut visited_fields = HashSet::new();

    let mut step_count = 0;
    let mut to_visit = HashSet::new();
    to_visit.insert(start);
    'outer: loop {
        let mut new_to_visit: HashSet<(usize, usize)> = HashSet::new();

        if to_visit.is_empty() {
            return 10000000;
        }
        for step in to_visit {
            // println!("visiting x: {} y: {}", step.0, step.1);
            if visited_fields.contains(&step) {
                continue;
            }
            visited_fields.insert(step);
            if step == end {
                break 'outer;
            }
            for dir in [(0, 1), (1, 0), (-1, 0), (0, -1)] {
                let next_step = (step.0 as i32 + dir.0, step.1 as i32 + dir.1);
                if next_step.0 >= 0 && next_step.0 < field[0].len() as i32 && next_step.1 >= 0 && next_step.1 < field.len() as i32 {
                    let cur_elevation = field[step.1][step.0];
                    let next_elevation = field[next_step.1 as usize][next_step.0 as usize];

                    if next_elevation <= cur_elevation + 1 {
                        let next_step = (next_step.0 as usize, next_step.1 as usize);
                        if !visited_fields.contains(&next_step) {
                            new_to_visit.insert(next_step);
                        }
                    }
                }
            }
        }

        to_visit = new_to_visit;
        step_count += 1;
    }

    step_count
}