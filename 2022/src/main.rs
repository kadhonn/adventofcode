#![allow(dead_code)]

extern crate core;

use std::fs;

mod day25;

fn main() {
    // let str = fs::read_to_string("in/in25_ex.txt").unwrap();
    let str = fs::read_to_string("in/in25.txt").unwrap();
    day25::day25_1(&str);
}