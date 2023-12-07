#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day7;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in7_ex.txt").unwrap();
    let str = fs::read_to_string("in/in7.txt").unwrap();
    day7::day7_1(&str.trim());
    println!("took {:?}", before.elapsed());
}