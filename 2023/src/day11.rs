use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day11(str: &str) {
    let mut map = vec![];
    let lines = str.lines().map(|line| line.chars().collect()).collect::<Vec<Vec<char>>>();

    let mut y_expand = 0;
    for y in 0..lines.len() {
        if (0..lines[y].len()).map(|i| lines[y][i]).all(|c| c == '.') {
            y_expand += 1;
        } else {
            let mut x_expand = 0;
            for x in 0..lines[y].len() {
                if (0..lines.len()).map(|i| lines[i][x]).all(|c| c == '.') {
                    x_expand += 1;
                }
                if lines[y][x] == '#' {
                    map.push((y + y_expand, x + x_expand));
                }
            }
        }
    }

    let mut sum = 0;
    for i in 0..map.len() {
        for j in i + 1..map.len() {
            let first = map[i];
            let second = map[j];
            sum += usize::abs_diff(first.0, second.0) + usize::abs_diff(first.1, second.1);
        }
    }

    println!("{sum}");
}
