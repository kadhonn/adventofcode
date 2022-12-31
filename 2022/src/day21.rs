use std::collections::HashMap;

pub fn day21_2(str: &String) {
    let monkeys = parse_input(str);

    let mut new_monkeys = HashMap::new();
    simplify_monkeys(&monkeys, &mut new_monkeys, "root");

    let mut monkeys = new_monkeys;

    let monkey = &monkeys["root"];
    let mut split = monkey.split(" + ");
    let first = split.next().unwrap().to_string();
    let second = split.next().unwrap().to_string();

    let wanted = resolve_monkey(&monkeys, &second);

    let mut max = 99999999999999;
    let mut min = 0;
    let mut i = 0;
    loop {
        i += 1;
        let cur_try: i64 = (max - min) / 2 + min;
        if i % 100 == 0 {
            println!("min: {}, max: {}, cur_try: {}", min, max, cur_try);
        }
        monkeys.insert("humn", cur_try.to_string());
        let res = resolve_monkey(&monkeys, first.as_str());
        if res == wanted {
            println!("FOUND: {}", cur_try);
            break;
        }
        if res < wanted {
            max = cur_try;
        } else {
            min = cur_try;
        }
    }
}

fn try_something(monkeys: &mut HashMap<&str, String>, first: &String, second: &String, mut i: i64) -> (i64, i64) {
    monkeys.insert("humn", i.to_string());
    println!("resolving first...");
    let first = resolve_monkey(&monkeys, first.as_str());
    println!("resolving second...");
    let second = resolve_monkey(&monkeys, second.as_str());
    println!("first:  {first}");
    println!("second: {second}");
    (first, second)
}

fn simplify_monkeys<'a>(monkeys: &'a HashMap<&'a str, String>, new_monkeys: &mut HashMap<&'a str, String>, monkey: &'a str) -> Option<i64> {
    if monkey == "humn" {
        new_monkeys.insert(monkey, "x".to_string());
        return None;
    }
    let result = &monkeys[monkey];
    if result.contains("+") {
        let mut split = result.split(" + ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        let first = simplify_monkeys(&monkeys, new_monkeys, first);
        let second = simplify_monkeys(&monkeys, new_monkeys, second);
        if first.is_none() || second.is_none() {
            new_monkeys.insert(monkey, result.to_string());
            return None;
        }
        let result = first.unwrap() + second.unwrap();
        new_monkeys.insert(monkey, result.to_string());
        return Some(result);
    }
    if result.contains("-") {
        let mut split = result.split(" - ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        let first = simplify_monkeys(&monkeys, new_monkeys, first);
        let second = simplify_monkeys(&monkeys, new_monkeys, second);
        if first.is_none() || second.is_none() {
            new_monkeys.insert(monkey, result.to_string());
            return None;
        }
        let result = first.unwrap() - second.unwrap();
        new_monkeys.insert(monkey, result.to_string());
        return Some(result);
    }
    if result.contains("/") {
        let mut split = result.split(" / ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        let first = simplify_monkeys(&monkeys, new_monkeys, first);
        let second = simplify_monkeys(&monkeys, new_monkeys, second);
        if first.is_none() || second.is_none() {
            new_monkeys.insert(monkey, result.to_string());
            return None;
        }
        let result = first.unwrap() / second.unwrap();
        new_monkeys.insert(monkey, result.to_string());
        return Some(result);
    }
    if result.contains("*") {
        let mut split = result.split(" * ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        let first = simplify_monkeys(&monkeys, new_monkeys, first);
        let second = simplify_monkeys(&monkeys, new_monkeys, second);
        if first.is_none() || second.is_none() {
            new_monkeys.insert(monkey, result.to_string());
            return None;
        }
        let result = first.unwrap() * second.unwrap();
        new_monkeys.insert(monkey, result.to_string());
        return Some(result);
    }

    new_monkeys.insert(monkey, result.to_string());
    return Some(result.parse().unwrap());
}

fn resolve_monkey(monkeys: &HashMap<&str, String>, monkey: &str) -> i64 {
    // println!("needing to resolve monkey {}", monkey);
    let result = &monkeys[monkey];
    if result.contains("+") {
        let mut split = result.split(" + ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        return resolve_monkey(&monkeys, first) + resolve_monkey(&monkeys, second);
    }
    if result.contains("-") {
        let mut split = result.split(" - ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        return resolve_monkey(&monkeys, first) - resolve_monkey(&monkeys, second);
    }
    if result.contains("/") {
        let mut split = result.split(" / ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        return resolve_monkey(&monkeys, first) / resolve_monkey(&monkeys, second);
    }
    if result.contains("*") {
        let mut split = result.split(" * ");
        let first = split.next().unwrap();
        let second = split.next().unwrap();
        return resolve_monkey(&monkeys, first) * resolve_monkey(&monkeys, second);
    }

    return result.parse().unwrap();
}

fn parse_input(str: &String) -> HashMap<&str, String> {
    let mut monkeys = HashMap::new();

    for line in str.lines() {
        let mut split = line.split(": ");
        let monkey = split.next().unwrap();
        let result = split.next().unwrap();
        monkeys.insert(monkey.trim(), result.trim().to_string());
    }

    monkeys
}