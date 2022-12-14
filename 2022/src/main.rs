#![allow(dead_code)]

use std::fs;

mod day7;

fn main() {
    // let str = fs::read_to_string("in/in7_ex.txt").unwrap();
    let str = fs::read_to_string("in/in7.txt").unwrap();
    day7::day7_2(&str.trim());
}