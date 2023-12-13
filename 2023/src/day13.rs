use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day13(str: &str) {
    let mut sum = 0;
    'fields: for field in str.split("\n\n") {
        let field: Vec<Vec<char>> = field.lines().map(|line| line.chars().collect()).collect();

        'line: for y in 0..field.len() - 1 {
            for i in 1..field.len() - y {
                if y < i - 1 {
                    break;
                }
                let y1 = y + i;
                let y2 = y - (i - 1);
                for x in 0..field[y].len() {
                    if field[y1][x] != field[y2][x] {
                        continue 'line;
                    }
                }
            }
            sum += (y + 1) * 100;
            continue 'fields;
        }

        'col: for x in 0..field[0].len() - 1 {
            for i in 1..field[0].len() - x {
                if x < i - 1 {
                    break;
                }
                let x1 = x + i;
                let x2 = x - (i - 1);
                for y in 0..field.len() {
                    if field[y][x1] != field[y][x2] {
                        continue 'col;
                    }
                }
            }
            sum += x + 1;
            continue 'fields;
        }
    }

    println!("{sum}");
}