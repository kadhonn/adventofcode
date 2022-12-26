#![allow(dead_code)]

use std::fs;

mod day22;

fn main() {
    // let str = fs::read_to_string("in/in22_ex.txt").unwrap();
    let str = fs::read_to_string("in/in22.txt").unwrap();
    day22::day22_2(&str);
}