#![allow(dead_code)]

use std::fs;

mod day13;

fn main() {
    // let str = fs::read_to_string("in/in13_ex.txt").unwrap();
    let str = fs::read_to_string("in/in13.txt").unwrap();
    day13::day13_2(&str.trim());
}