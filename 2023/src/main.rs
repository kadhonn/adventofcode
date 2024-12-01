#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day23;

fn main() {
    let before = Instant::now();
    let str = fs::read_to_string("in/in23_ex.txt").unwrap();
    // let str = fs::read_to_string("in/in23.txt").unwrap();
    day23::day23(&str.trim());
    println!("took {:?}", before.elapsed());
}