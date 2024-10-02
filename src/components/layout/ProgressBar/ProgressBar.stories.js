import { ProgressBar } from ".";

export default {
  title: "Components/ProgressBar",
  component: ProgressBar,
  argTypes: {
    value: {
      options: [
        "sixty",
        "zero",
        "thirty",
        "eighty",
        "twenty",
        "one-hundred",
        "fifty",
        "ten",
        "forty",
        "ninety",
        "seventy",
      ],
      control: { type: "select" },
    },
  },
};

export const Default = {
  args: {
    value: "sixty",
    className: {},
    trackClassName: {},
    filledClassName: {},
  },
};
