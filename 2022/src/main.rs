#![allow(dead_code)]

use std::fs;

mod day10;

fn main() {
    // let str = fs::read_to_string("in/in10_ex.txt").unwrap();
    let str = fs::read_to_string("in/in10.txt").unwrap();
    day10::day10_2(&str.trim());
}