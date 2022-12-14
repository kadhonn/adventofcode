#![allow(dead_code)]

use std::fs;

mod day8;

fn main() {
    // let str = fs::read_to_string("in/in8_ex.txt").unwrap();
    let str = fs::read_to_string("in/in8.txt").unwrap();
    day8::day8_2(&str.trim());
}