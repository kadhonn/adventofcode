use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::f32::consts::E;
use std::i64::MAX;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use priority_queue::PriorityQueue;
use regex::Regex;


const NORTH: (i64, i64) = (0, -1);
const SOUTH: (i64, i64) = (0, 1);
const EAST: (i64, i64) = (1, 0);
const WEST: (i64, i64) = (-1, 0);

pub fn day18(str: &str) {
    //start/end/dir/crosses
    let dig_plan: Vec<(i64, (i64, i64))> = str.lines().map(|line| {
        let split: Vec<&str> = line.split(" ").collect();
        let hexa = split[2];
        let dir_command = &hexa[7..8];
        let hexa_steps = &hexa[2..7];
        let steps = i64::from_str_radix(hexa_steps, 16).unwrap();
        let dir = match dir_command {
            "0" => EAST,
            "2" => WEST,
            "3" => NORTH,
            "1" => SOUTH,
            _ => panic!("uh oh")
        };
        // let dir_command = split[0];
        // let steps = split[1].parse::<i64>().unwrap();
        // let dir = match dir_command {
        //     "R" => EAST,
        //     "L" => WEST,
        //     "U" => NORTH,
        //     "D" => SOUTH,
        //     _ => panic!("uh oh")
        // };
        (steps, dir)
    }).collect();
    let mut trenches: Vec<((i64, i64), (i64, i64), (i64, i64), bool)> = vec![];
    let mut prev_pos = (0i64, 0i64);
    let mut prev_dir = dig_plan[dig_plan.len() - 1].1;
    for i in 0..dig_plan.len() {
        let (steps, dir) = dig_plan[i];
        let (_, next_dir) = dig_plan[(i + 1) % dig_plan.len()];

        let x = prev_pos.0 + steps * dir.0;
        let y = prev_pos.1 + steps * dir.1;
        let pos = (x, y);
        let crosses = prev_dir == next_dir;
        if dir == next_dir || prev_dir == dir || (-dir.0 == -next_dir.0 && -dir.1 == -next_dir.1) || (-dir.0 == -prev_dir.0 && -dir.1 == -prev_dir.1) {
            panic!("uh oh");
        }

        trenches.push((prev_pos, pos, dir, crosses));

        prev_pos = pos;
        prev_dir = dir;
    }

    let mut crossings: Vec<((i64, i64), (i64, i64), bool)> = vec![];
    for (start, end, dir, crosses) in trenches {
        if dir == NORTH || dir == SOUTH {
            let (min, max) = if dir == SOUTH {
                (start, end)
            } else {
                (end, start)
            };
            crossings.push(((min.0, min.1 + 1), (max.0, max.1 - 1), true));
        } else {
            let (min, max) = if dir == EAST {
                (start, end)
            } else {
                (end, start)
            };
            crossings.push(((min.0, min.1), (max.0, max.1), crosses));
        }
    }


    let y_values = find_all_y(&crossings);

    let mut count = 0i64;
    for i in 0..y_values.len() {
        let y = y_values[i];
        let x_values = find_all_x(&crossings, y);
        if x_values.is_empty() {
            break;
        }
        let mut row_count = 0i64;
        let mut prev_x = 0;
        let mut inside = false;
        for j in 0..x_values.len() {
            let x = x_values[j];
            if x.2 {
                if inside {
                    row_count += x.1 - prev_x + 1;
                } else {
                    prev_x = x.0;
                }
                inside = !inside;
            } else {
                if !inside {
                    row_count += x.1 - x.0 + 1;
                }
            }
        }
        if inside {
            panic!("uh oh");
        }
        let rows = if i + 1 < y_values.len() {
            y_values[i + 1] - y
        } else {
            1
        };
        count += rows * row_count;
    }

    println!("{count}");
}

fn find_all_y(crossings: &Vec<((i64, i64), (i64, i64), bool)>) -> Vec<i64> {
    let mut y_values = HashSet::new();
    for crossing in crossings {
        y_values.insert(crossing.0.1);
        y_values.insert(crossing.0.1 + 1);
        y_values.insert(crossing.1.1);
        y_values.insert(crossing.1.1 + 1);
    }

    let mut sorted: Vec<i64> = y_values.into_iter().collect();
    sorted.sort();
    return sorted;
}

fn find_all_x(crossings: &Vec<((i64, i64), (i64, i64), bool)>, y: i64) -> Vec<(i64, i64, bool)> {
    let mut x_values = HashSet::new();
    for crossing in crossings {
        if crossing.0.1 <= y && y <= crossing.1.1 {
            x_values.insert((crossing.0.0, crossing.1.0, crossing.2));
        }
    }

    let mut sorted: Vec<(i64, i64, bool)> = x_values.into_iter().collect();
    sorted.sort_by_key(|crossing| crossing.0);
    return sorted;
}