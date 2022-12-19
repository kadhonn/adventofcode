#![allow(dead_code)]

use std::fs;

mod day14;

fn main() {
    // let str = fs::read_to_string("in/in14_ex.txt").unwrap();
    let str = fs::read_to_string("in/in14.txt").unwrap();
    day14::day14_2(&str.trim());
}