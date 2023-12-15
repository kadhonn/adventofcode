use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day14(str: &str) {
    let mut field: Vec<Vec<char>> = str.lines().map(|line| line.chars().collect()).collect();

    let mut known_state = HashMap::new();
    let mut i = 0;
    let goal = 1000000000;
    while i < goal {
        if known_state.contains_key(&field) {
            let diff = i - known_state[&field];
            if diff == 0 {
                panic!("wat")
            }
            let skip = ((goal - i) / diff) * diff;
            i = i + skip;
            known_state.clear();
        } else {
            known_state.insert(field.clone(), i);
            spin_field(&mut field);
            // println!("{i}");
            // print_and_calc(&mut field);
            // println!();
            i += 1;
        }
    }

    print_and_calc(&mut field);
}

fn print_and_calc(field: &mut Vec<Vec<char>>) {
    let mut weight = 0;
    for y in 0..field.len() {
        for x in 0..field[y].len() {
            print!("{}", field[y][x]);
            if field[y][x] == 'O' {
                weight += field.len() - y;
            }
        }
        println!();
    }

    println!("{weight}");
}

fn spin_field(field: &mut Vec<Vec<char>>) {
    //north
    for y in 0..field.len() {
        for x in 0..field[y].len() {
            if field[y][x] == 'O' {
                let mut i = y;
                while i > 0 && field[i - 1][x] == '.' {
                    i -= 1;
                }
                field[y][x] = '.';
                field[i][x] = 'O';
            }
        }
    }
    //west
    for x in 0..field[0].len() {
        for y in 0..field.len() {
            if field[y][x] == 'O' {
                let mut i = x;
                while i > 0 && field[y][i - 1] == '.' {
                    i -= 1;
                }
                field[y][x] = '.';
                field[y][i] = 'O';
            }
        }
    }
    //south
    for y in (0..field.len()).rev() {
        for x in 0..field[y].len() {
            if field[y][x] == 'O' {
                let mut i = y;
                while i < field.len() - 1 && field[i + 1][x] == '.' {
                    i += 1;
                }
                field[y][x] = '.';
                field[i][x] = 'O';
            }
        }
    }
    //east
    for x in (0..field[0].len()).rev() {
        for y in 0..field.len() {
            if field[y][x] == 'O' {
                let mut i = x;
                while i < field.len() - 1 && field[y][i + 1] == '.' {
                    i += 1;
                }
                field[y][x] = '.';
                field[y][i] = 'O';
            }
        }
    }
}