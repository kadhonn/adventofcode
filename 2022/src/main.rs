#![allow(dead_code)]

extern crate core;

use std::fs;

mod day20;

fn main() {
    // let str = fs::read_to_string("in/in20_ex.txt").unwrap();
    let str = fs::read_to_string("in/in20.txt").unwrap();
    day20::day20_1(&str);
}