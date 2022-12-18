type Operation = dyn Fn(i64) -> i64;

struct Monkey {
    nr: i64,
    items: Vec<i64>,
    op: Box<Operation>,
    div: i64,
    monkey_true: i64,
    monkey_false: i64,
    count: i64,
}

impl Monkey {
    fn new(nr: i64,
           items: Vec<i64>,
           op: Box<Operation>,
           div: i64,
           monkey_true: i64,
           monkey_false: i64) -> Monkey {
        Monkey {
            nr,
            items,
            op,
            div,
            monkey_true,
            monkey_false,
            count: 0,
        }
    }
}

pub fn day11_2(str: &str) {
    let mut monkeys = parse_monkeys(str);

    let prod:i64 = monkeys.iter().map(|m| m.div).product();

    for _ in 0..10000 {
        for i in 0..monkeys.len() {
            for j in 0..monkeys[i].items.len() {
                monkeys[i].count += 1;
                let item = monkeys[i].items[j];
                let item: i64 = monkeys[i].op.as_ref()(item);
                let item = item % prod;
                let new_monkey = if item % monkeys[i].div == 0 {
                    monkeys[i].monkey_true
                } else {
                    monkeys[i].monkey_false
                } as usize;
                monkeys[new_monkey].items.push(item);
            }

            monkeys[i].items.clear();
        }
    }

    let mut counts: Vec<i64> = monkeys.iter().map(|monkey| monkey.count).collect();
    counts.sort();
    println!("{}", counts[counts.len() - 1] * counts[counts.len() - 2]);
}

pub fn day11_1(str: &str) {
    let mut monkeys = parse_monkeys(str);

    for _ in 0..20 {
        for i in 0..monkeys.len() {
            for j in 0..monkeys[i].items.len() {
                monkeys[i].count += 1;
                let item = monkeys[i].items[j];
                let item: i64 = monkeys[i].op.as_ref()(item);
                let item = item / 3;
                let new_monkey = if item % monkeys[i].div == 0 {
                    monkeys[i].monkey_true
                } else {
                    monkeys[i].monkey_false
                } as usize;
                monkeys[new_monkey].items.push(item);
            }

            monkeys[i].items.clear();
        }
    }

    let mut counts: Vec<i64> = monkeys.iter().map(|monkey| monkey.count).collect();
    counts.sort();
    println!("{}", counts[counts.len() - 1] * counts[counts.len() - 2]);
}

fn parse_monkeys(str: &str) -> Vec<Monkey> {
    let mut monkeys = vec![];

    let mut lines = str.lines();
    loop {
        let has_more = lines.next();
        if let None = has_more {
            break;
        }
        let monkey = has_more.unwrap();
        if monkey.len() == 0 {
            break;
        }
        let monkey_nr: i64 = monkey[7..monkey.len() - 1].parse().unwrap();

        let start_items = &lines.next().unwrap()[18..];
        let mut items = vec!();
        for item in start_items.split(", ") {
            items.push(item.parse().unwrap());
        }

        let op = lines.next().unwrap();
        let second = &op[25..];
        let op = &op[23..24];
        let op = match op {
            "*" => {
                if second.eq("old") {
                    Box::new(|old| old * old) as Box<Operation>
                } else {
                    let second = second.to_string();
                    Box::new(move |old| old * second.parse::<i64>().unwrap()) as Box<Operation>
                }
            }
            "+" => {
                let second = second.to_string();
                Box::new(move |old| old + second.parse::<i64>().unwrap()) as Box<Operation>
            }
            _ => {
                panic!("unknown op {op}")
            }
        };

        let div = lines.next().unwrap()[21..].parse().unwrap();
        let monkey_true = lines.next().unwrap()[29..].parse().unwrap();
        let monkey_false = lines.next().unwrap()[30..].parse().unwrap();


        let monkey = Monkey::new(monkey_nr, items, op, div, monkey_true, monkey_false);
        monkeys.push(monkey);

        lines.next();
    }
    monkeys
}