#![allow(dead_code)]

extern crate core;

use std::fs;

mod day3;

fn main() {
    // let str = fs::read_to_string("in/in3_ex.txt").unwrap();
    let str = fs::read_to_string("in/in3.txt").unwrap();
    day3::day3_1(&str.trim());
}