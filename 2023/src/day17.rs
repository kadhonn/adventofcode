use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::f32::consts::E;
use std::i64::MAX;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use priority_queue::PriorityQueue;
use regex::Regex;

const NORTH: (i32, i32) = (-1, 0);
const SOUTH: (i32, i32) = (1, 0);
const EAST: (i32, i32) = (0, 1);
const WEST: (i32, i32) = (0, -1);

pub fn day17(str: &str) {
    let field: Vec<Vec<i32>> = str.lines().map(|line| line.chars().map(|c| c.to_digit(10).unwrap() as i32).collect()).collect();

    let mut next_steps = PriorityQueue::new();
    let mut visited_fields = HashMap::new();
    //((y, x), (y_dir, x_dir), length)
    next_steps.push(((0i32, 0i32), EAST, 0i32, 0i32, vec![]), i32::MAX);

    while !next_steps.is_empty() {
        let (((y, x), (y_dir, x_dir), length, cost, path), _) = next_steps.pop().unwrap();
        if y == field.len() as i32 - 1 && x == field[y as usize].len() as i32 - 1 {
            println!("{cost}");
            for y in 0..field.len() {
                for x in 0..field[y].len() {
                    if path.contains(&(y as i32, x as i32)) {
                        print!("#");
                    } else {
                        print!(".");
                    }
                }
                println!();
            }
            return;
        }

        if visited_fields.contains_key(&((y, x), (y_dir, x_dir), length)) {
            if visited_fields[&((y, x), (y_dir, x_dir), length)] <= cost {
                continue;
            }
        }
        visited_fields.insert(((y, x), (y_dir, x_dir), length), cost);

        for ((new_y, new_x), (new_y_dir, new_x_dir), new_length) in get_steps(y, x, y_dir, x_dir, length) {
            if new_y < 0 || new_y >= field.len() as i32 || new_x < 0 || new_x >= field[0].len() as i32 {
                continue;
            }
            let new_coord = (new_y, new_x);
            let new_cost = cost + field[new_y as usize][new_x as usize];

            let mut new_path = path.clone();
            new_path.push(new_coord);
            let option = next_steps.push((new_coord, (new_y_dir, new_x_dir), new_length, new_cost, new_path), i32::MAX - new_cost);
            if option.is_some() {
                panic!("uh oh");
            }
        }
    }
    panic!("FUCK");
}

fn get_steps(y: i32, x: i32, y_dir: i32, x_dir: i32, length: i32) -> Vec<((i32, i32), (i32, i32), i32)> {
    let mut steps = vec![];

    if (y_dir, x_dir) == NORTH || (y_dir, x_dir) == SOUTH {
        steps.push(((y + WEST.0, x + WEST.1), WEST, 1));
        steps.push(((y + EAST.0, x + EAST.1), EAST, 1));
    } else {
        steps.push(((y + NORTH.0, x + NORTH.1), NORTH, 1));
        steps.push(((y + SOUTH.0, x + SOUTH.1), SOUTH, 1));
    }
    if length < 3 {
        steps.push(((y + y_dir, x + x_dir), (y_dir, x_dir), length + 1));
    }

    return steps;
}
