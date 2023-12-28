#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day20;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in20_ex.txt").unwrap();
    let str = fs::read_to_string("in/in20.txt").unwrap();
    day20::day20(&str.trim());
    println!("took {:?}", before.elapsed());
}