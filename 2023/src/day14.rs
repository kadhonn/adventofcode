use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day14(str: &str) {
    let mut field: Vec<Vec<char>> = str.lines().map(|line| line.chars().collect()).collect();

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

    let mut weight = 0;
    for y in 0..field.len() {
        for x in 0..field[y].len() {
            if field[y][x] == 'O' {
                weight += field.len() - y;
            }
        }
    }
    println!("{weight}");
}