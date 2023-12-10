use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day10(str: &str) {
    let map = str
        .lines()
        .map(|line| line.chars().collect::<Vec<char>>())
        .collect::<Vec<Vec<char>>>();

    let mut visited = HashMap::new();
    let mut next_nodes = LinkedList::new();
    let start_pos = find_start_pos(&map);
    next_nodes.push_back((0, start_pos));
    visited.insert(start_pos, 0);

    let mut max_steps = 0;
    while !next_nodes.is_empty() {
        let (steps, next_node) = next_nodes.pop_front().unwrap();
        for step in get_next_steps(&map, next_node) {
            if !visited.contains_key(&step) {
                next_nodes.push_back((steps + 1, step));
                visited.insert(step, steps + 1);
            }
        }

        max_steps = max(steps, max_steps);
    }

    for y in 0..map.len() {
        for x in 0..map[y].len() {
            let key = (x as i32, y as i32);
            if visited.contains_key(&key) {
                print!("{}", visited[&key]);
            } else {
                print!("{}", map[y][x]);
            }
        }
        println!();
    }

    println!("{max_steps}");
}

fn get_next_steps(map: &Vec<Vec<char>>, pos: (i32, i32)) -> Vec<(i32, i32)> {
    let x = pos.0;
    let y = pos.1;
    let symbol = map[y as usize][x as usize];
    return match symbol {
        '|' => vec![(x, y + 1), (x, y - 1)],
        '-' => vec![(x + 1, y), (x - 1, y)],
        'L' => vec![(x, y - 1), (x + 1, y)],
        'J' => vec![(x, y - 1), (x - 1, y)],
        '7' => vec![(x, y + 1), (x - 1, y)],
        'F' => vec![(x, y + 1), (x + 1, y)],
        'S' => {
            let mut steps = vec![];

            if y > 0 {
                let symbol_north = map[y as usize - 1][x as usize];
                if symbol_north == '|' || symbol_north == '7' || symbol_north == 'F' {
                    steps.push((x, y - 1));
                }
            }
            if y < (map.len() as i32 - 1) {
                let symbol_south = map[y as usize + 1][x as usize];
                if symbol_south == '|' || symbol_south == 'J' || symbol_south == 'L' {
                    steps.push((x, y + 1));
                }
            }
            if x < (map[0].len() as i32 - 1) {
                let symbol_east = map[y as usize][x as usize + 1];
                if symbol_east == '-' || symbol_east == '7' || symbol_east == 'J' {
                    steps.push((x + 1, y));
                }
            }
            if x > 0 {
                let symbol_west = map[y as usize][x as usize - 1];
                if symbol_west == '-' || symbol_west == 'F' || symbol_west == 'L' {
                    steps.push((x - 1, y));
                }
            }

            if steps.len() != 2 {
                panic!("steps for S is wrong size");
            }

            steps
        }
        _ => panic!("unknown symbol {symbol}"),
    };
}

fn find_start_pos(map: &Vec<Vec<char>>) -> (i32, i32) {
    for y in 0..map.len() {
        for x in 0..map[y].len() {
            if map[y][x] == 'S' {
                return (x as i32, y as i32);
            }
        }
    }
    panic!("díd not find start pos");
}
