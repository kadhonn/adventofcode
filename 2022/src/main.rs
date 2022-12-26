#![allow(dead_code)]

use std::fs;

mod day17;

fn main() {
    // let str = fs::read_to_string("in/in17_ex.txt").unwrap();
    let str = fs::read_to_string("in/in17.txt").unwrap();
    day17::day17_1(&str);
}