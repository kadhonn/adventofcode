#![allow(dead_code)]

use std::fs;

mod day15;

fn main() {
    // let str = fs::read_to_string("in/in15_ex.txt").unwrap();
    let str = fs::read_to_string("in/in15.txt").unwrap();
    day15::day15_2(&str.trim());
}