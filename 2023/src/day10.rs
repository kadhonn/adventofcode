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

    let mut inside: HashSet<(i32, i32)> = HashSet::new();
    let mut visited = HashMap::new();
    let mut next_nodes = LinkedList::new();
    let start_pos = find_start_pos(&map);
    next_nodes.push_back((0, start_pos));
    visited.insert(start_pos, 0);

    while !next_nodes.is_empty() {
        let (steps, next_node) = next_nodes.pop_front().unwrap();
        for step in get_next_steps(&map, next_node) {
            if !visited.contains_key(&step) {
                next_nodes.push_back((steps + 1, step));
                visited.insert(step, steps + 1);
            }
        }
    }

    for y in 0..map.len() {
        for x in 0..map[y].len() {
            let key = (x as i32, y as i32);
            if !visited.contains_key(&key) && is_inside(&map, &visited, &key) {
                inside.insert(key);
            }
        }
    }

    for y in 0..map.len() {
        for x in 0..map[y].len() {
            let key = (x as i32, y as i32);
            if visited.contains_key(&key) {
                print!("{}", map[y][x]);
            } else if inside.contains(&key) {
                print!("#");
            } else {
                print!(".");
            }
        }
        println!();
    }

    println!("{}", inside.len());
}

fn is_inside(map: &Vec<Vec<char>>, visited: &HashMap<(i32, i32), i32>, key: &(i32, i32)) -> bool {
    let x = key.0;
    let y = key.1;

    let mut i;

    if x == 2 && y == 6 {
        println!("wub");
    }
    let mut count_west = 0;
    i = x - 1;
    while i >= 0 {
        if visited.contains_key(&(i, y)) {
            let start_char = map[y as usize][i as usize];
            i -= 1;
            while i >= 0 {
                let char = map[y as usize][i as usize];
                if !visited.contains_key(&(i, y)) || (char != '-' && char != 'F' && char != 'L') {
                    break;
                }
                i -= 1;
            }
            i += 1;
            if !(start_char == '7' && map[y as usize][i as usize] == 'F' || start_char == 'J' && map[y as usize][i as usize] == 'L') {
                count_west += 1;
            }
        }
        i -= 1;
    }

    let mut count_north = 0;
    i = y - 1;
    while i >= 0 {
        if visited.contains_key(&(x, i)) {
            let start_char = map[i as usize][x as usize];
            i -= 1;
            while i >= 0 {
                let char = map[i as usize][x as usize];
                if !visited.contains_key(&(x, i)) || (char != '|' && char != 'F' && char != '7') {
                    break;
                }
                i -= 1;
            }
            i += 1;
            if !(start_char == 'J' && map[i as usize][x as usize] == '7' || start_char == 'L' && map[i as usize][x as usize] == 'F') {
                count_north += 1;
            }
        }
        i -= 1;
    }

    return count_north % 2 == 1 && count_west % 2 == 1;
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
