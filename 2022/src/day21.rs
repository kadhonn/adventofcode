use std::collections::HashMap;

pub fn day21_1(str: &String) {
    let mut monkeys = parse_input(str);

    let result = resolve_monkey(&monkeys, "root");

    println!("{}", result);
}

pub fn day21_2(str: &String) {
    let mut monkeys = parse_input(str);

    let monkey = &monkeys["root"];
    let mut split = monkey.split(" + ");
    let first = split.next().unwrap().to_string();
    let second = split.next().unwrap().to_string();

    let mut i = 0;
    loop {
        monkeys.insert("humn", i.to_string());
        let result = resolve_monkey(&monkeys, first.as_str()) == resolve_monkey(&monkeys, second.as_str());
        if result {
            break;
        }
        i+=1;
    }

    println!("{}", i);
}

fn resolve_monkey(monkeys: &HashMap<&str, String>, monkey: &str) -> i64 {
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