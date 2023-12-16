use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::f32::consts::E;
use std::i64::MAX;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use regex::Regex;

const NORTH: (i32, i32) = (-1, 0);
const SOUTH: (i32, i32) = (1, 0);
const EAST: (i32, i32) = (0, 1);
const WEST: (i32, i32) = (0, -1);

pub fn day16(str: &str) {
    let field: Vec<Vec<char>> = str.lines().map(|line| line.chars().collect()).collect();
    let mut visited_states = HashSet::new();
    let mut states = LinkedList::new();
    states.push_back(((0, -1), EAST));

    while !states.is_empty() {
        let state = states.pop_front().unwrap();
        let new_states = get_next_states(&field, state);
        for new_state in new_states {
            if !visited_states.contains(&new_state) {
                visited_states.insert(new_state);
                states.push_back(new_state);
            }
        }
    }

    let visited_fields = visited_states.iter().map(|it| (*it).0).collect::<HashSet<(i32, i32)>>();
    for y in 0..field.len() {
        for x in 0..field.len() {
            if visited_fields.contains(&(y as i32, x as i32)) {
                print!("#");
            } else {
                print!(".");
            }
        }
        println!();
    }
    let sum = visited_fields.len();
    println!("{sum}");
}

fn get_next_states(field: &Vec<Vec<char>>, state: ((i32, i32), (i32, i32))) -> Vec<((i32, i32), (i32, i32))> {
    let new_y = state.0.0 + state.1.0;
    let new_x = state.0.1 + state.1.1;
    if new_y < 0 || new_y >= field.len() as i32 || new_x < 0 || new_x >= field[new_y as usize].len() as i32 {
        return vec![];
    }

    let new_field = field[new_y as usize][new_x as usize];
    if new_field == '.'
        || (new_field == '-' && (state.1 == WEST || state.1 == EAST))
        || (new_field == '|' && (state.1 == NORTH || state.1 == SOUTH)) {
        return vec![((new_y, new_x), state.1)];
    }
    if new_field == '/' {
        let new_dir = match state.1 {
            NORTH => EAST,
            SOUTH => WEST,
            EAST => NORTH,
            WEST => SOUTH,
            _ => panic!("uh oh")
        };
        return vec![((new_y, new_x), new_dir)];
    }
    if new_field == '\\' {
        let new_dir = match state.1 {
            NORTH => WEST,
            SOUTH => EAST,
            EAST => SOUTH,
            WEST => NORTH,
            _ => panic!("uh oh")
        };
        return vec![((new_y, new_x), new_dir)];
    }
    if new_field == '-' {
        return vec![
            ((new_y, new_x), EAST),
            ((new_y, new_x), WEST),
        ];
    }
    if new_field == '|' {
        return vec![
            ((new_y, new_x), NORTH),
            ((new_y, new_x), SOUTH),
        ];
    }
    panic!("uh oh, missed handling {new_field}")
}
