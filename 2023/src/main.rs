#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day14;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in14_ex.txt").unwrap();
    let str = fs::read_to_string("in/in14.txt").unwrap();
    day14::day14(&str.trim());
    println!("took {:?}", before.elapsed());
}